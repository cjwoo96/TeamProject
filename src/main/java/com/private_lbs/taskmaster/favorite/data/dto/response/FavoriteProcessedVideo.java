package com.private_lbs.taskmaster.favorite.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteProcessedVideo {
    private String processedVideoUrl;
    private String hitterName;
    private String pitcherName;
    private boolean favorite;
}