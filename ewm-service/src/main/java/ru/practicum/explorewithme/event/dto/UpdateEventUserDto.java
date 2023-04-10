package ru.practicum.explorewithme.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEventUserDto extends UpdateEventDto {
    private StateAction stateAction;

    public enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }
}
