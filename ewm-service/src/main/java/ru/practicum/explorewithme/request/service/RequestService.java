package ru.practicum.explorewithme.request.service;

import java.util.List;
import ru.practicum.explorewithme.request.dto.ChangeRequestStatusDto;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.dto.RequestStatusesDto;

public interface RequestService {

  List<RequestDto> getRequests(long userId);

  RequestDto createRequest(long userId, long eventId);

  RequestDto cancelRequest(long userId, long requestId);

  List<RequestDto> getEventRequests(long userId, long eventId);

  RequestStatusesDto changeEventRequests(long userId, long eventId, ChangeRequestStatusDto changeRequestStatusDto);
}
