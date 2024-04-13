package com.webapp.socialmedia.controller.message;

import lombok.Builder;

@Builder
public record Message(String to, String message, String from) {
}
