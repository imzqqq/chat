/* add an "ordering" column to background_updates, which can be used to sort them
   to achieve some level of consistency. */

ALTER TABLE background_updates ADD COLUMN ordering INT NOT NULL DEFAULT 0;
