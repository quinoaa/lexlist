package space.quinoaa.lexlist.data;

import lombok.With;

@With
public record User(long id, String username, String password) {
}
