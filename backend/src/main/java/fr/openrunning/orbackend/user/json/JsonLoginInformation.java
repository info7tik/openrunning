package fr.openrunning.orbackend.user.json;

import lombok.Getter;

public class JsonLoginInformation {
    @Getter
    private String email;
    @Getter
    private String password;
}
