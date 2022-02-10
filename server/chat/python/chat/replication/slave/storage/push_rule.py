from chat.replication.slave.storage._slaved_id_tracker import SlavedIdTracker
from chat.replication.tcp.streams import PushRulesStream
from chat.storage.databases.main.push_rule import PushRulesWorkerStore

from .events import SlavedEventStore


class SlavedPushRuleStore(SlavedEventStore, PushRulesWorkerStore):
    def get_max_push_rules_stream_id(self):
        return self._push_rules_stream_id_gen.get_current_token()

    def process_replication_rows(self, stream_name, instance_name, token, rows):
        # We assert this for the benefit of mypy
        assert isinstance(self._push_rules_stream_id_gen, SlavedIdTracker)

        if stream_name == PushRulesStream.NAME:
            self._push_rules_stream_id_gen.advance(instance_name, token)
            for row in rows:
                self.get_push_rules_for_user.invalidate((row.user_id,))
                self.get_push_rules_enabled_for_user.invalidate((row.user_id,))
                self.push_rules_stream_cache.entity_has_changed(row.user_id, token)
        return super().process_replication_rows(stream_name, instance_name, token, rows)
