package com.des06.ai_v1.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record BookRequest(
        @NotBlank @Size(max = 30) String isbn,
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 150) String author,
        @NotEmpty @Size(max = 8)
        Set<@NotBlank @Size(max = 50) String> topics) {}

