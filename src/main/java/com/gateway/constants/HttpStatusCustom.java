package com.gateway.constants;

import org.springframework.lang.Nullable;

public enum HttpStatusCustom {

    INTERNAL_SERVER_ERROR(500, Series.SERVER_ERROR, "Service Down");

    private static final HttpStatusCustom[] VALUES = values();

    private final int value;
    private final Series series;
    private final String reasonPhrase;

    HttpStatusCustom(int value, Series series, String reasonPhrase) {
        this.value = value;
        this.series = series;
        this.reasonPhrase = reasonPhrase;
    }

    public static HttpStatusCustom valueOf(int statusCode) {
        HttpStatusCustom status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return status;
    }

    @Nullable
    public static HttpStatusCustom resolve(int statusCode) {
        for (HttpStatusCustom status : VALUES) {
            if (status.value == statusCode) {
                return status;
            }
        }
        return null;
    }

    public int value() {
        return this.value;
    }

    public Series series() {
        return this.series;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public boolean is1xxInformational() {
        return this.series == Series.INFORMATIONAL;
    }

    public boolean is2xxSuccessful() {
        return this.series == Series.SUCCESSFUL;
    }

    public boolean is3xxRedirection() {
        return this.series == Series.REDIRECTION;
    }

    public boolean is4xxClientError() {
        return this.series == Series.CLIENT_ERROR;
    }

    public boolean is5xxServerError() {
        return this.series == Series.SERVER_ERROR;
    }

    public boolean isError() {
        return is4xxClientError() || is5xxServerError();
    }

    @Override
    public String toString() {
        return this.value + " " + this.name();
    }

    public enum Series {
        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        Series(int value) {
            this.value = value;
        }

        public static Series valueOf(int statusCode) {
            Series series = resolve(statusCode);
            if (series == null) {
                throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
            }
            return series;
        }

        @Nullable
        public static Series resolve(int statusCode) {
            int seriesCode = statusCode / 100;
            for (Series series : values()) {
                if (series.value == seriesCode) {
                    return series;
                }
            }
            return null;
        }

        public int value() {
            return this.value;
        }
    }
}

