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
package mage.sets.magic2015;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.dynamicvalue.common.PermanentsOnBattlefieldCount;
import mage.abilities.effects.PreventionEffectImpl;
import mage.abilities.keyword.EquipAbility;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;


/**
 *
 * @author emerald000
 */
public class ShieldOfTheAvatar extends CardImpl {

    public ShieldOfTheAvatar(UUID ownerId) {
        super(ownerId, 230, "Shield of the Avatar", Rarity.RARE, new CardType[]{CardType.ARTIFACT}, "{1}");
        this.expansionSetCode = "M15";
        this.subtype.add("Equipment");

        // If a source would deal damage to equipped creature, prevent X of that damage, where X is the number of creatures you control.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new ShieldOfTheAvatarPreventionEffect()));
        
        // Equip {2}
        this.addAbility(new EquipAbility(Outcome.AddAbility, new GenericManaCost(2)));
    }

    public ShieldOfTheAvatar(final ShieldOfTheAvatar card) {
        super(card);
    }

    @Override
    public ShieldOfTheAvatar copy() {
        return new ShieldOfTheAvatar(this);
    }
}

class ShieldOfTheAvatarPreventionEffect extends PreventionEffectImpl {
    
    private static final FilterControlledCreaturePermanent filter = new FilterControlledCreaturePermanent("creatures you control");
    
    ShieldOfTheAvatarPreventionEffect() {
        super(Duration.WhileOnBattlefield);
        this.staticText = "If a source would deal damage to equipped creature, prevent X of that damage, where X is the number of creatures you control.";
    }

    ShieldOfTheAvatarPreventionEffect(final ShieldOfTheAvatarPreventionEffect effect) {
        super(effect);
    }

    @Override
    public ShieldOfTheAvatarPreventionEffect copy() {
        return new ShieldOfTheAvatarPreventionEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public boolean replaceEvent(GameEvent event, Ability source, Game game) {
        boolean result = false;
        Permanent equipment = game.getPermanent(source.getSourceId());
        if (equipment != null && equipment.getAttachedTo() != null) {
            int numberOfCreaturesControlled = new PermanentsOnBattlefieldCount(filter).calculate(game, source);
            int toPrevent = Math.min(numberOfCreaturesControlled, event.getAmount());
            GameEvent preventEvent = new GameEvent(GameEvent.EventType.PREVENT_DAMAGE, equipment.getAttachedTo(), source.getId(), source.getControllerId(), toPrevent, false);
            if (!game.replaceEvent(preventEvent)) {
                if (event.getAmount() >= toPrevent) {
                    event.setAmount(event.getAmount() - toPrevent);
                }
                else {
                    event.setAmount(0);
                    result = true;
                }
                if (toPrevent > 0) {
                    game.informPlayers(new StringBuilder("Shield of the Avatar ").append("prevented ").append(toPrevent).append(" damage to ").append(game.getPermanent(equipment.getAttachedTo()).getName()).toString());
                    game.fireEvent(GameEvent.getEvent(GameEvent.EventType.PREVENTED_DAMAGE,
                            equipment.getAttachedTo(), source.getSourceId(), source.getControllerId(), toPrevent));
                }
            }
        }
        return result;
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        if (super.applies(event, source, game)) {
            Permanent equipment = game.getPermanent(source.getSourceId());
            if (equipment != null && equipment.getAttachedTo() != null
                    && event.getTargetId().equals(equipment.getAttachedTo())) {
                return true;
            }
        }
        return false;
    }
}