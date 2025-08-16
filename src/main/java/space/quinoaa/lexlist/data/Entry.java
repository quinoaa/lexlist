package space.quinoaa.lexlist.data;

import lombok.With;

@With
public record Entry(String name, long dictid, String data) {
}
