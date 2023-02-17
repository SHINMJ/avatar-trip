package com.avatar.trip.plan.party.domain;

import com.avatar.trip.plan.party.domain.Party;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class Parties {

    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Party> parties = new ArrayList<>();

    public boolean contains(Party party){
        return this.parties.contains(party);
    }

    public void addParty(Party party){
        if (!contains(party)){
            this.parties.add(party);
        }
    }

    public void removeParty(Party party){
        this.parties.remove(party);
    }

    public boolean canEdit(Long userId) {
        return this.parties.stream()
            .anyMatch(party -> party.edit(userId));
    }

    public boolean canRead(Long userId) {
        return this.parties.stream()
            .anyMatch(party -> party.isOwner(userId));
    }
}
