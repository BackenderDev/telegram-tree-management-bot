TRUNCATE child_element CASCADE;
TRUNCATE root_element CASCADE;
ALTER SEQUENCE child_element_id_seq RESTART WITH 1;
ALTER SEQUENCE root_element_id_seq RESTART WITH 1;