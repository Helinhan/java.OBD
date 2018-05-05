package com.hantong.message;

import java.util.Date;

public class RequestMessage {

    public RequestMessage() {
        this.receiveDate = new Date();
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public ReportMessage getReport() {
        return report;
    }

    public void setReport(ReportMessage report) {
        this.report = report;
    }

    public CommandMessage getCommand() {
        return command;
    }

    public void setCommand(CommandMessage command) {
        this.command = command;
    }

    public AcknowledgeMessage getAcknowledge() {
        return acknowledge;
    }

    public void setAcknowledge(AcknowledgeMessage acknowledge) {
        this.acknowledge = acknowledge;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    private String hardwareId;
    private MessageType type;
    private Date   eventDate;
    private Date   receiveDate;
    private ReportMessage report;
    private CommandMessage command;
    private AcknowledgeMessage acknowledge;
}
