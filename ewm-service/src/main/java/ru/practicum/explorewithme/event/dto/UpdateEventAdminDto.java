package ru.practicum.explorewithme.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEventAdminDto extends UpdateEventDto {
    private StateAction stateAction;
    public enum StateAction {
        PUBLISH_EVENT,
        REJECT_EVENT
    }
}
