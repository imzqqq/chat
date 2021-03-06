# frozen_string_literal: true

require_relative '../mastodon/snowflake'

module ActiveRecord
  module Tasks
    module DatabaseTasks
      original_load_schema = instance_method(:load_schema)

      define_method(:load_schema) do |db_config, *args|
        ActiveRecord::Base.establish_connection(db_config)
        Flow::Snowflake.define_timestamp_id

        original_load_schema.bind(self).call(db_config, *args)

        Flow::Snowflake.ensure_id_sequences_exist
      end
    end
  end
end
