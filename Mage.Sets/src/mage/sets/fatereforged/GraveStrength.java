/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.fatereforged;

import java.util.UUID;
import mage.abilities.dynamicvalue.common.CardsInControllerGraveyardCount;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.PutTopCardOfLibraryIntoGraveControllerEffect;
import mage.abilities.effects.common.counter.AddCountersTargetEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Rarity;
import mage.counters.CounterType;
import mage.filter.common.FilterCreatureCard;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author LevelX2
 */
public class GraveStrength extends CardImpl {

    public GraveStrength(UUID ownerId) {
        super(ownerId, 71, "Grave Strength", Rarity.UNCOMMON, new CardType[]{CardType.SORCERY}, "{1}{B}");
        this.expansionSetCode = "FRF";

        // Choose target creature. Put the top three cards of your library into your graveyard, then put a +1/+1 counter on that creature for each creature card in your graveyard.
        this.getSpellAbility().addTarget(new TargetCreaturePermanent());
        Effect effect = new PutTopCardOfLibraryIntoGraveControllerEffect(3);
        effect.setText("Choose target creature. Put the top three cards of your library into your graveyard");
        this.getSpellAbility().addEffect(effect);
        effect = new AddCountersTargetEffect(CounterType.P1P1.createInstance(0), new CardsInControllerGraveyardCount(new FilterCreatureCard()));
        effect.setText(", then put a +1/+1 counter on that creature for each creature card in your graveyard");
        this.getSpellAbility().addEffect(effect);

    }

    public GraveStrength(final GraveStrength card) {
        super(card);
    }

    @Override
    public GraveStrength copy() {
        return new GraveStrength(this);
    }
}
