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
package mage.sets.oathofthegatewatch;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.keyword.SupportEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.TargetController;
import mage.counters.CounterType;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.permanent.ControllerPredicate;
import mage.filter.predicate.permanent.CounterPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.Target;
import mage.target.common.TargetCreaturePermanent;
import mage.target.targetpointer.SecondTargetPointer;

/**
 *
 * @author LevelX2
 */
public class NissasJudgment extends CardImpl {

    public NissasJudgment(UUID ownerId) {
        super(ownerId, 139, "Nissa's Judgment", Rarity.UNCOMMON, new CardType[]{CardType.SORCERY}, "{4}{G}");
        this.expansionSetCode = "OGW";

        // Support 2.
        getSpellAbility().addEffect(new SupportEffect(this, 2, false));

        // Choose up to one target creature an opponent controls. Each creature you control with a +1/+1 counter on it deals damage equal to its power to that creature.
        Effect effect = new NissasJudgmentEffect();
        effect.setTargetPointer(new SecondTargetPointer()); // First target is used by Support
        getSpellAbility().addEffect(effect);
    }

    public NissasJudgment(final NissasJudgment card) {
        super(card);
    }

    @Override
    public NissasJudgment copy() {
        return new NissasJudgment(this);
    }
}

class NissasJudgmentEffect extends OneShotEffect {

    private final static FilterCreaturePermanent filter = new FilterCreaturePermanent("creature an opponent controls");
    private final static FilterCreaturePermanent filterWithCounter = new FilterCreaturePermanent();

    static {
        filter.add(new ControllerPredicate(TargetController.OPPONENT));
        filterWithCounter.add(new CounterPredicate(CounterType.P1P1));
    }

    public NissasJudgmentEffect() {
        super(Outcome.Damage);
        this.staticText = "Choose up to one target creature an opponent controls. Each creature you control with a +1/+1 counter on it deals damage equal to its power to that creature";
    }

    public NissasJudgmentEffect(final NissasJudgmentEffect effect) {
        super(effect);
    }

    @Override
    public NissasJudgmentEffect copy() {
        return new NissasJudgmentEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            Target target = new TargetCreaturePermanent(0, 1, filter, true);
            if (controller.choose(outcome, target, source.getSourceId(), game)) {
                Permanent targetCreature = game.getPermanent(target.getFirstTarget());
                if (targetCreature != null) {
                    for (Permanent permanent : game.getBattlefield().getAllActivePermanents(filterWithCounter, controller.getId(), game)) {
                        if (permanent.getPower().getValue() > 0) {
                            targetCreature.damage(permanent.getPower().getValue(), permanent.getId(), game, false, true);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
