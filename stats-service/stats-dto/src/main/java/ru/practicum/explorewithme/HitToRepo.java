package ru.practicum.explorewithme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HitToRepo {
    private String app;
    private String uri;
    private Long hits;
}
