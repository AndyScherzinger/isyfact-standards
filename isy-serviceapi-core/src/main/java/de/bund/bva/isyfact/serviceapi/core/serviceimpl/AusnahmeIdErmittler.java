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
package de.bund.bva.isyfact.serviceapi.core.serviceimpl;

/**
 * Ermittelt zu einer aufgetretenen Exception eine passende Ausnahme-ID, die den Fehlertext identifiziert.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use <a href="https://isyfact.github.io/isyfact-standards-doku/latest/isy-service-rest/konzept/master.html">REST according to IsyFacts REST Concept</a> instead.
 */
@Deprecated
public interface AusnahmeIdErmittler {

    /**
     * Ermittelt zu einer aufgetretenen Exception eine passende Ausnahme-ID, die den Fehlertext identifiziert.
     * 
     * @param e
     *            die aufgetretene Exception
     * @return die Ausnahme-ID, nicht null
     */
    String ermittleAusnahmeId(Throwable e);

}
