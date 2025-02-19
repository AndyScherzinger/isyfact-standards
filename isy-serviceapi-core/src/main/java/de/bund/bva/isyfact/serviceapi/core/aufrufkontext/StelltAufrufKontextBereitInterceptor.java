/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.bund.bva.isyfact.serviceapi.core.aufrufkontext;

import java.util.Optional;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

import de.bund.bva.isyfact.aufrufkontext.AufrufKontext;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextFactory;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.pliscommon.serviceapi.service.httpinvoker.v1_0_0.AufrufKontextTo;

/**
 * Creates a {@link AufrufKontext}, fills it with {@link AufrufKontextTo}, and sets it
 * in the {@link AufrufKontextVerwalter}.
 *
 * @param <T> the AufrufKontext
 */
public class StelltAufrufKontextBereitInterceptor<T extends AufrufKontext> implements MethodInterceptor,
    Ordered {

    /** Logger. */
    private static final IsyLogger LOG = IsyLoggerFactory
        .getLogger(StelltAufrufKontextBereitInterceptor.class);

    /**
     * Default order for interceptor execution, if no other is set.
     * Must be executed before GesichertInterceptor.
     */
    private static final int DEFAULT_ORDER = 10_000;

    /**
     * Access the AufrufKontextFactory to create the application specific AufrufKontext.
     */
    private final AufrufKontextFactory<T> aufrufKontextFactory;

    /** Access to AufrufKontextVerwalter, to set the AufrufKontext. */
    private final AufrufKontextVerwalter<T> aufrufKontextVerwalter;

    /** Resolver for AufrufKontextTo from parameter list. */
    private final AufrufKontextToResolver aufrufKontextToResolver;

    /** Order for interceptor execution. */
    private int order = DEFAULT_ORDER;

    public StelltAufrufKontextBereitInterceptor(
        AufrufKontextFactory<T> aufrufKontextFactory,
        AufrufKontextVerwalter<T> aufrufKontextVerwalter,
        AufrufKontextToResolver aufrufKontextToResolver) {
        this.aufrufKontextFactory = aufrufKontextFactory;
        this.aufrufKontextVerwalter = aufrufKontextVerwalter;
        this.aufrufKontextToResolver = aufrufKontextToResolver;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        T alterAufrufKontext = aufrufKontextVerwalter.getAufrufKontext();

        Optional<AufrufKontextTo> aufrufKontextToOptional =
            aufrufKontextToResolver.leseAufrufKontextTo(invocation.getArguments());

        if (!aufrufKontextToOptional.isPresent()) {
            LOG.debug("Es wurde kein AufrufKontext uebermittelt.");
        }

        T aufrufKontext = aufrufKontextToOptional
            .map(this::mapToGenericAufrufKontext)
            .orElse(null);

        aufrufKontextVerwalter.setAufrufKontext(aufrufKontext);

        try {
            return invocation.proceed();
        } finally {
            aufrufKontextVerwalter.setAufrufKontext(alterAufrufKontext);
        }
    }

    private T mapToGenericAufrufKontext(AufrufKontextTo aufrufKontextTo) {
        T aufrufKontext = aufrufKontextFactory.erzeugeAufrufKontext();

        // map AufrufKontextTo to AufrufKontext
        aufrufKontext.setDurchfuehrendeBehoerde(aufrufKontextTo.getDurchfuehrendeBehoerde());
        aufrufKontext.setDurchfuehrenderBenutzerKennung(aufrufKontextTo
            .getDurchfuehrenderBenutzerKennung());
        aufrufKontext.setDurchfuehrenderBenutzerPasswort(aufrufKontextTo
            .getDurchfuehrenderBenutzerPasswort());
        aufrufKontext.setDurchfuehrenderSachbearbeiterName(aufrufKontextTo
            .getDurchfuehrenderSachbearbeiterName());
        aufrufKontext.setKorrelationsId(aufrufKontextTo.getKorrelationsId());
        aufrufKontext.setRolle(aufrufKontextTo.getRolle());
        aufrufKontext.setRollenErmittelt(aufrufKontextTo.isRollenErmittelt());

        aufrufKontextFactory.nachAufrufKontextVerarbeitung(aufrufKontext);
        return aufrufKontext;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
