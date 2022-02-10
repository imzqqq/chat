/*
 * Opentracing context data for inclusion in the device_list_update EDUs, as a
 * json-encoded dictionary. NULL if opentracing is disabled (or not enabled for this destination).
 */
ALTER TABLE device_lists_outbound_pokes ADD opentracing_context TEXT;
