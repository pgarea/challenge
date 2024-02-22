package es.pgomez.tc.testing.util.auth;

import java.util.Objects;

public class TokenDTO {

    private String token;

    public TokenDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TokenDTO tokenDTO = (TokenDTO) object;
        return Objects.equals(token, tokenDTO.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
