package space.quinoaa.lexlist.data;

import lombok.With;

@With
public record Dictionary(long dictid, long ownerid, String name) {
}
