import type Modeler from "bpmn-js/lib/Modeler";
import type { WetBpmnProcessType } from "../types";

export const createNewDiagram = async (
	modelerIns: Modeler,
	params: {
		id?: string;
		name?: string;
		type?: WetBpmnProcessType;
		xml?: string;
	}
) => {
	const TYPE_TARGET = {
		activiti: "http://activiti.org/bpmn",
		camunda: "http://camunda.org/schema/1.0/bpmn",
		flowable: "http://flowable.org/bpmn"
	};

	const { id, name, namespace, xml } = {
		id: new Date().getTime(),
		name: `业务流程_${new Date().getTime()}`,
		namespace: TYPE_TARGET[params.type || "camunda"],
		...params
	};
	const $xml =
		xml ||
		`<?xml version="1.0" encoding="UTF-8"?>
    <bpmn2:definitions
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL"
    xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
    xmlns:dc="http://www.omg.org/spec/DD/20100524/DC"
    xmlns:di="http://www.omg.org/spec/DD/20100524/DI"
    xsi:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd"
    id="diagram_${id}"
    targetNamespace="${namespace}">
      <bpmn2:process id="Process_${id}" name="${name}" isExecutable="true">
      </bpmn2:process>
      <bpmndi:BPMNDiagram id="BPMNDiagram_1">
        <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_${id}">
        </bpmndi:BPMNPlane>
      </bpmndi:BPMNDiagram>
    </bpmn2:definitions>`;

	try {
		await modelerIns.importXML($xml);
	} catch (err) {
		console.error(err);
	}
};
