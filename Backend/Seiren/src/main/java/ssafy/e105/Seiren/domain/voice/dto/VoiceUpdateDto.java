package ssafy.e105.Seiren.domain.voice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VoiceUpdateDto {

    private Long voiceId;
    private String voiceTitle;
    private String memo;
    private String voiceAvatarUrl;
}