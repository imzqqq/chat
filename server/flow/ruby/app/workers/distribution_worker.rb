# frozen_string_literal: true

class DistributionWorker
  include Sidekiq::Worker

  def perform(status_id)
    RedisLock.acquire(redis: Redis.current, key: "distribute:#{status_id}", autorelease: 5.minutes.seconds) do |lock|
      if lock.acquired?
        FanOutOnWriteService.new.call(Status.find(status_id))
      else
        raise Flow::RaceConditionError
      end
    end
  rescue ActiveRecord::RecordNotFound
    true
  end
end
