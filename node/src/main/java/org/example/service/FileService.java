package org.example.service;

import org.example.entity.AppDocument;
import org.example.entity.AppPhoto;
import org.example.service.enums.LinkType;
import org.json.JSONException;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message externalMessage) throws JSONException;
    AppPhoto processPhoto(Message externalMessage) throws JSONException;
    String generateLink(Long docId, LinkType linkType);
}
