/*
 * Copyright (c) 2015 ZTE, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ofconfig.southbound.impl.topology.impl.ofconfig12;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import org.junit.Test;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.ofconfig.southbound.impl.OFconfigTestBase;
import org.opendaylight.ofconfig.southbound.impl.OfconfigConstants;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddressBuilder;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.PortNumber;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.CapableSwitch;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.CapableSwitchBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.DatapathIdType;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.OFConfigId;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.OFControllerType.Protocol;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.capableswitchtype.LogicalSwitchesBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.capableswitchtype.logical.switches.Switch;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.capableswitchtype.logical.switches.SwitchBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.capableswitchtype.logical.switches.SwitchKey;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.oflogicalswitchtype.ControllersBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.oflogicalswitchtype.controllers.Controller;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.oflogicalswitchtype.controllers.ControllerBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.config.yang.rev150211.oflogicalswitchtype.controllers.ControllerKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ofconfig.topology.rev150901.OfconfigLogicalSwitchAugmentation;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NetworkTopology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NodeId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.NodeKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;



/**
 * @author rui hu hu.rui2@zte.com.cn
 *
 */
public class LogicalSwitchTopoNodeAddHelperTest extends OFconfigTestBase {


    @Test
    public void test_create_logicSwitch_Topo_node() {


        LogicalSwitchTopoNodeAddHelper logicalSwitchTopoNodeAddHelper =
                new LogicalSwitchTopoNodeAddHelper();

        NodeId netconfNodeId = new NodeId("test_switch");

        CapableSwitchBuilder cswBuilder = new CapableSwitchBuilder();

        LogicalSwitchesBuilder lswBuilder = new LogicalSwitchesBuilder();

        List<Switch> swlist = new ArrayList<>();
        SwitchBuilder swBuilder = new SwitchBuilder();

        ControllersBuilder ctlllerBuilder = new ControllersBuilder();

        List<Controller> ctllers = new ArrayList<>();

        ControllerBuilder builder = new ControllerBuilder();



        builder.setId(new OFConfigId("test_ctl"))
                .setKey(new ControllerKey(new OFConfigId("test_ctl"))).setProtocol(Protocol.Tcp)
                .setIpAddress(IpAddressBuilder.getDefaultInstance("127.0.0.1"))
                .setPort(PortNumber.getDefaultInstance("6630"));

        ctllers.add(builder.build());

        ctlllerBuilder.setController(ctllers);

        swBuilder.setId(new OFConfigId("test_sw")).setKey(new SwitchKey(new OFConfigId("test_sw")))
                .setControllers(ctlllerBuilder.build())
                .setDatapathId(new DatapathIdType("00:00:7a:31:cd:91:04:40"));

        swlist.add(swBuilder.build());

        lswBuilder.setSwitch(swlist);


        cswBuilder.setId("test_capableSwitch").setConfigVersion("12")
                .setLogicalSwitches(lswBuilder.build());


        ReadWriteTransaction tx = databroker.newReadWriteTransaction();

        CapableSwitch cpsw = cswBuilder.build();

        logicalSwitchTopoNodeAddHelper.addLogicalSwitchTopoNodeAttributes(netconfNodeId,
                Optional.of(cpsw), tx);


        try {
            tx.submit().checkedGet();
        } catch (TransactionCommitFailedException e1) {
            e1.printStackTrace();
            fail(e1.getMessage());
        }

        String nodeStringprefix = netconfNodeId.getValue() + ":" + cpsw.getId() + ":" + "test_sw";

        NodeId logicaSwNodeId = new NodeId(nodeStringprefix);
        NodeKey nodeKey = new NodeKey(logicaSwNodeId);

        InstanceIdentifier<Node> iid = InstanceIdentifier.builder(NetworkTopology.class)
                .child(Topology.class,
                        new TopologyKey(OfconfigConstants.OFCONFIG_LOGICAL_TOPOLOGY_ID))
                .child(Node.class, nodeKey).build();


        Node node = mdsalUtils.read(LogicalDatastoreType.OPERATIONAL, iid, databroker);

        OfconfigLogicalSwitchAugmentation logicSwitchNode =
                node.getAugmentation(OfconfigLogicalSwitchAugmentation.class);


        assertEquals("test_ctl", logicSwitchNode.getOfconfigLogicalSwitchAttributes()
                .getLogicalSwitch().getControllers().getController().get(0).getId().getValue());


    }

}
