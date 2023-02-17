package com.avatar.trip.plan.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Note {

    @Column(columnDefinition = "LONGTEXT")
    private String note;

    private Note(String note) {
        this.note = note;
    }

    public static Note valueOf(String note){
        return new Note(note);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Note note1 = (Note) o;
        return note.equals(note1.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(note);
    }

    @Override
    public String toString() {
        return note;
    }
}
