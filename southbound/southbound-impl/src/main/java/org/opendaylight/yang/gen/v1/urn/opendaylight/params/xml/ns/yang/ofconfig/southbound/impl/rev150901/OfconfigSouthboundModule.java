/*
 * Copyright (c) 2015 ZTE, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ofconfig.southbound.impl.rev150901;

import org.opendaylight.ofconfig.southbound.impl.OfconfigSouthboundImpl;

public class OfconfigSouthboundModule extends
        org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ofconfig.southbound.impl.rev150901.AbstractOfconfigSouthboundModule {
    public OfconfigSouthboundModule(
            org.opendaylight.controller.config.api.ModuleIdentifier identifier,
            org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public OfconfigSouthboundModule(
            org.opendaylight.controller.config.api.ModuleIdentifier identifier,
            org.opendaylight.controller.config.api.DependencyResolver dependencyResolver,
            org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ofconfig.southbound.impl.rev150901.OfconfigSouthboundModule oldModule,
            java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        final OfconfigSouthboundImpl ofconfigSBImpl = new OfconfigSouthboundImpl();

        getBrokerDependency().registerProvider(ofconfigSBImpl);

        return new AutoCloseable() {

            @Override
            public void close() throws Exception {
                ofconfigSBImpl.close();
            }
        };
    }

}
