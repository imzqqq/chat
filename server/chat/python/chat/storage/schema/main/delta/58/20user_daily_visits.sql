-- Add new column to user_daily_visits to track user agent
ALTER TABLE user_daily_visits
    ADD COLUMN user_agent TEXT;
