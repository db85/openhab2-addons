package org.openhab.binding.parrotflower.internal.api;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("email")
    private String email;
    @SerializedName("language_iso639")
    private String languageIso639;
    @SerializedName("notification_curfew_end")
    private String notificationCurfewEnd;
    @SerializedName("notification_curfew_start")
    private String notificationCurfewStart;
    @SerializedName("pictures_public")
    private String picturesPublic;
    @SerializedName("use_fahrenheit")
    private String useFahrenheit;
    @SerializedName("useFeetInches")
    private String useFeetInches;
    @SerializedName("use_ftc")
    private String useFtc;
    @SerializedName("use_liter")
    private String useLiter;
    @SerializedName("use_lux")
    private String useLux;
    @SerializedName("use_mol")
    private String useMol;
    @SerializedName("utc_timezone")
    private String utcTimezone;

    public String getEmail() {
        return email;
    }

    public String getLanguageIso639() {
        return languageIso639;
    }

    public String getNotificationCurfewEnd() {
        return notificationCurfewEnd;
    }

    public String getNotificationCurfewStart() {
        return notificationCurfewStart;
    }

    public String getPicturesPublic() {
        return picturesPublic;
    }

    public String getUseFahrenheit() {
        return useFahrenheit;
    }

    public String getUseFeetInches() {
        return useFeetInches;
    }

    public String getUseFtc() {
        return useFtc;
    }

    public String getUseLiter() {
        return useLiter;
    }

    public String getUseLux() {
        return useLux;
    }

    public String getUseMol() {
        return useMol;
    }

    public String getUtcTimezone() {
        return utcTimezone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLanguageIso639(String languageIso639) {
        this.languageIso639 = languageIso639;
    }

    public void setNotificationCurfewEnd(String notificationCurfewEnd) {
        this.notificationCurfewEnd = notificationCurfewEnd;
    }

    public void setNotificationCurfewStart(String notificationCurfewStart) {
        this.notificationCurfewStart = notificationCurfewStart;
    }

    public void setPicturesPublic(String picturesPublic) {
        this.picturesPublic = picturesPublic;
    }

    public void setUseFahrenheit(String useFahrenheit) {
        this.useFahrenheit = useFahrenheit;
    }

    public void setUseFeetInches(String useFeetInches) {
        this.useFeetInches = useFeetInches;
    }

    public void setUseFtc(String useFtc) {
        this.useFtc = useFtc;
    }

    public void setUseLiter(String useLiter) {
        this.useLiter = useLiter;
    }

    public void setUseLux(String useLux) {
        this.useLux = useLux;
    }

    public void setUseMol(String useMol) {
        this.useMol = useMol;
    }

    public void setUtcTimezone(String utcTimezone) {
        this.utcTimezone = utcTimezone;
    }

}
