package com.yzy.entity;


public class MailTask {

    private Integer task_id;
    private String recipient_mail_address = "";
    private String recipient_cc_mail_address = "";
    private String mail_text = "";
    private String mail_subject = "";

    @Override
    public String toString() {
        return "MailTask{" +
                "task_id=" + task_id +
                ", recipient_mail_address='" + recipient_mail_address + '\'' +
                ", recipient_cc_mail_address='" + recipient_cc_mail_address + '\'' +
                ", mail_text='" + mail_text + '\'' +
                ", mail_subject='" + mail_subject + '\'' +
                '}';
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getRecipient_mail_address() {
        return recipient_mail_address;
    }

    public void setRecipient_mail_address(String recipient_mail_address) {
        this.recipient_mail_address = recipient_mail_address;
    }

    public String getRecipient_cc_mail_address() {
        return recipient_cc_mail_address;
    }

    public void setRecipient_cc_mail_address(String recipient_cc_mail_address) {
        this.recipient_cc_mail_address = recipient_cc_mail_address;
    }

    public String getMail_text() {
        return mail_text;
    }

    public void setMail_text(String mail_text) {
        this.mail_text = mail_text;
    }

    public String getMail_subject() {
        return mail_subject;
    }

    public void setMail_subject(String mail_subject) {
        this.mail_subject = mail_subject;
    }
}
