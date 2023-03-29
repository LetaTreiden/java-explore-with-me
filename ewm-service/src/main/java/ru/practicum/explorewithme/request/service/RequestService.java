package ru.practicum.explorewithme.request.service;

import java.util.List;
import ru.practicum.explorewithme.request.dto.UpdateRequestDto;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.dto.RequestStatusesDto;

public interface RequestService {

  List<RequestDto> getAll(long userId);

  RequestDto create(long userId, long eventId);

  RequestDto cancel(long userId, long requestId);

  List<RequestDto> getAllById(long userId, long eventId);

  RequestStatusesDto update(long userId, long eventId, UpdateRequestDto updateRequestDto);
}
