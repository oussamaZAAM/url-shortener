package com.url.shortener.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "urls")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UrlEntity implements Serializable {

    @Id
    private String id;
    private String originalUrl;
    private String shortUrl;
}
