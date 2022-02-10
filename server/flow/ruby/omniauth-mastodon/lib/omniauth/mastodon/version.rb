module OmniAuth
  module Flow
    module Version
      module_function

      def major
        0
      end

      def minor
        9
      end

      def patch
        3
      end

      def pre
        nil
      end

      def to_a
        [major, minor, patch, pre].compact
      end

      def to_s
        to_a.join('.')
      end
    end
  end
end
