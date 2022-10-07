package by.mrsomfergo.easynewsfeed.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDto {
    String fileName;

    String fileDownloadUri;

    Long fileSize;

    public static FileDto makeDefault(String fileName, String fileDownloadUri, Long fileSize) {
        return builder()
                .fileName(fileName)
                .fileDownloadUri(fileDownloadUri)
                .fileSize(fileSize)
                .build();
    }
}
