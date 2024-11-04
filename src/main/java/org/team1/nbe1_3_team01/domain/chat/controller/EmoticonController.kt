package org.team1.nbe1_3_team01.domain.chat.controller

import org.springframework.core.io.ResourceLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.IOException
import java.util.stream.Collectors
import java.util.stream.Stream

@RestController
@RequestMapping("/api/emoticons")
class EmoticonController(
    private val resourceLoader: ResourceLoader
) {

    @get:Throws(IOException::class)
    @get:GetMapping
    val emoticons: List<String>
        // 이모티콘 조회용 메소드
        get() {
            val emoticonPath = "classpath:static/emoticons/"
            val folder = resourceLoader.getResource(emoticonPath).file

            // 폴더 내의 모든 파일 URL 변환 후 주소로 변경
            return Stream.of(*folder.listFiles())
                .map { obj: File -> obj.name }
                .map { name: String -> "/emoticons/$name" } // ex) /emoticons/1.jpg, /emoticons/2.jpg ...
                .collect(Collectors.toList())
        }
}
