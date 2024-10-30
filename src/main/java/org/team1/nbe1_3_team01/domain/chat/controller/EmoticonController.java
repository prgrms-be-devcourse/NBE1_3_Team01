package org.team1.nbe1_3_team01.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/emoticons")
@RequiredArgsConstructor
public class EmoticonController {

    private final ResourceLoader resourceLoader;

    // 이모티콘 조회용 메소드
    @GetMapping
    public List<String> getEmoticons() throws IOException {
        String emoticonPath = "classpath:static/emoticons/";
        File folder = resourceLoader.getResource(emoticonPath).getFile();

        // 폴더 내의 모든 파일 URL 변환 후 주소로 변경
        return Stream.of(folder.listFiles())
                .map(File::getName)
                .map(name -> "/emoticons/" + name) // ex) /emoticons/1.jpg, /emoticons/2.jpg ...
                .collect(Collectors.toList());
    }
}
