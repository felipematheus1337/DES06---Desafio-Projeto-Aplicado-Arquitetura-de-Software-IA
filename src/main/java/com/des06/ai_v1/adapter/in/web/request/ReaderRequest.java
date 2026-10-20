package com.des06.ai_v1.adapter.in.web.request;

public record ReaderRequest(
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Email @Size(max = 200) String email) {}
