<template>
	<div class="card">
		<FullCalendar ref="calendarRef" :options="calendarOptions"></FullCalendar>
	</div>
</template>

<script setup lang="ts">
import { ref, Ref } from "vue";
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import { DateClickArg } from "@fullcalendar/interaction";
import { CalendarOptions } from "@fullcalendar/core";
import { listSysRestDay4FCApi, addSysRestDayApi, deleteSysRestDayApi } from "@/api/modules/sys/restday";
import dayjs from "dayjs";

const calendarRef = ref<InstanceType<typeof FullCalendar> | null>(null);

const handleDateClick = (info: DateClickArg) => {
	handleUpdateEvent({
		id: info.dateStr,
		title: "休",
		start: info.date,
		allDay: true,
		color: "#6aba49",
		textColor: "#fff"
	});
};

const handleUpdateEvent = (newEventData: any) => {
	const calendarApi = calendarRef.value?.getApi();
	if (calendarApi) {
		const existingEvent = calendarApi.getEvents().find(event => event.startStr === newEventData.id);
		if (existingEvent) {
			// 删除
			existingEvent.remove();
			deleteSysRestDayApi({ date: newEventData.id });
		} else {
			// 新增
			calendarApi.addEvent(newEventData);
			addSysRestDayApi({ date: newEventData.id });
		}
	}
};

const calendarOptions: Ref<CalendarOptions> = ref({
	locale: "zh-cn",
	contentHeight: 530,
	events: async (fetchInfo, successCallback) => {
		// 先清空旧面板事件
		const calendarApi = calendarRef.value?.getApi();
		if (calendarApi) {
			calendarApi.getEvents().forEach(event => event.remove());
		}
		// 再刷新新面板事件
		const { data } = await listSysRestDay4FCApi({
			start: dayjs(fetchInfo.start).format("YYYY-MM-DD"),
			end: dayjs(fetchInfo.end).format("YYYY-MM-DD")
		});
		successCallback(data);
	},
	plugins: [dayGridPlugin, interactionPlugin],
	initialView: "dayGridMonth",
	headerToolbar: {
		left: "prev,next today",
		center: "title",
		right: "prevYear,nextYear"
	},
	buttonText: {
		prev: "上一月",
		next: "下一月",
		prevYear: "上一年",
		nextYear: "下一年",
		today: "今天"
	},
	dayCellContent: ({ date }) => {
		return {
			html: `${date.getDate()}`
		};
	},
	dateClick: handleDateClick
});
</script>

<style lang="scss" scoped>
.fc {
	:deep(.fc-header-toolbar) {
		font-size: 14px;
	}

	:deep(table) {
		.fc-header-toolbar {
			font-size: 14px;
		}

		thead > tr > th div {
			font-weight: bold;
			color: #ed6d23;
			font-size: 14px;
		}

		.fc-day-sat .fc-daygrid-day-number,
		.fc-day-sat .fc-col-header-cell-cushion,
		.fc-day-sun .fc-daygrid-day-number,
		.fc-day-sun .fc-col-header-cell-cushion {
			color: #25992e !important;
		}

		.fc-daygrid-day-top .fc-daygrid-day-number {
			width: 100%;
			text-align: right;
			font-weight: 600;
			padding: 12px 12px 0 12px;
		}

		.fc-daygrid-day {
			cursor: pointer;
		}

		.fc-daygrid-event {
			z-index: -1;
		}
	}
}
</style>
