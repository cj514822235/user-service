package com.tw.userservice.modle;



public enum Level {

        EASY("suitable for beginners"),
        MEDIUM("towards to senior"),
        HARD("suitable for senior"),
        HELL("team to solve");

        private final String description;

        Level(String description) {
                this.description = description;
        }

        public String getDescription() {
                return description;
        }
}
