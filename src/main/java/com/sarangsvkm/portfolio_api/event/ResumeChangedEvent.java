package com.sarangsvkm.portfolio_api.event;

import org.springframework.context.ApplicationEvent;

public class ResumeChangedEvent extends ApplicationEvent {
    public ResumeChangedEvent(Object source) {
        super(source);
    }
}
