CREATE INDEX events_order_topo_stream_room ON events(
    topological_ordering, stream_ordering, room_id
);
