package com.example.bsk.mail;

import java.util.LinkedList;
import java.util.List;
import okhttp3.MultipartBody;

public class MailgunEmail {

    public final Contact from;
    public final List<Contact> to;
    public final String subject;
    public final String text;
    public final String tag;
    public final long deliveryTime;

    private MailgunEmail(Contact from, List<Contact> to, String subject, String text,
                         String tag, long deliveryTime) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.tag = tag;
        this.deliveryTime = deliveryTime;
    }

    public MultipartBody toMultipartBody() {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("from", from.toString())
                .addFormDataPart("subject", subject);

        if (text != null) bodyBuilder.addFormDataPart("text", text);

        for (Contact contact : to) {
            bodyBuilder.addFormDataPart("to", contact.toString());
        }

        if (tag != null) bodyBuilder.addFormDataPart("tag", tag);

        return bodyBuilder.build();
    }

    public static class Contact {
//        public final String name;
        public final String email;

        public Contact(String email) {
//            this.name = name;
            this.email = email;
        }

        @Override public String toString() {
//            return String.format("%s <%s>", name, email);
            return String.format("<%s>", email);
        }
    }

    public static class Builder {
        private Contact from;
        private List<Contact> to = new LinkedList<>();
        private String subject;
        private String text;
        private String tag;
        private long deliveryTime;

        public Builder from(Contact from) {
            this.from = from;
            return this;
        }

        public Builder addTo(Contact contact) {
            this.to.add(contact);
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder deliveryTime(long deliveryTime) {
            this.deliveryTime = deliveryTime;
            return this;
        }

        public MailgunEmail build() {
            return new MailgunEmail(from, to, subject, text, tag, deliveryTime);
        }
    }
}