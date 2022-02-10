-- we need to do this first due to foreign constraints
DROP TABLE IF EXISTS application_services_regex;

DROP TABLE IF EXISTS application_services;
DROP TABLE IF EXISTS transaction_id_to_pdu;
DROP TABLE IF EXISTS stats_reporting;
DROP TABLE IF EXISTS current_state_resets;
DROP TABLE IF EXISTS event_content_hashes;
DROP TABLE IF EXISTS event_destinations;
DROP TABLE IF EXISTS event_edge_hashes;
DROP TABLE IF EXISTS event_signatures;
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS room_hosts;
DROP TABLE IF EXISTS server_tls_certificates;
DROP TABLE IF EXISTS state_forward_extremities;
