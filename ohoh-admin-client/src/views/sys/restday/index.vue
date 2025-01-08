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
import solarLunar from "solarLunar-es";

// 节假日标题
const restDayTitle = "休";

const calendarRef = ref<InstanceType<typeof FullCalendar> | null>(null);

const handleDateClick = (info: DateClickArg) => {
	handleUpdateEvent({
		id: info.dateStr,
		title: restDayTitle,
		start: info.date,
		allDay: true,
		color: "#6aba49",
		textColor: "#fff"
	});
};

const handleUpdateEvent = (newEventData: any) => {
	const calendarApi = calendarRef.value?.getApi();
	if (calendarApi) {
		const existingEvent = calendarApi
			.getEvents()
			.find(event => event.startStr === newEventData.id && event.title == restDayTitle);
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
		// 请求接口，获取日期期间的休息日fullcalendar event数据
		const { data } = await listSysRestDay4FCApi({
			start: dayjs(fetchInfo.start).format("YYYY-MM-DD"),
			end: dayjs(fetchInfo.end).format("YYYY-MM-DD")
		});
		// 组装节日event
		for (const date = fetchInfo.start; date <= fetchInfo.end; date.setDate(date.getDate() + 1)) {
			const lunarDate = solarLunar.solar2lunar(date.getFullYear(), date.getMonth() + 1, date.getDate());
			if (typeof lunarDate != "number" && lunarDate.festival) {
				data.push({
					title: lunarDate.festival,
					start: dayjs(date).format("YYYY-MM-DD")
				});
			}
			if (typeof lunarDate != "number" && lunarDate.lunarFestival) {
				data.push({
					title: lunarDate.lunarFestival,
					start: dayjs(date).format("YYYY-MM-DD")
				});
			}
		}
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
		const lunarDate = solarLunar.solar2lunar(date.getFullYear(), date.getMonth() + 1, date.getDate());
		if (typeof lunarDate != "number") {
			return {
				html: `<div style="float:left">
								${lunarDate.lDay === 1 ? lunarDate.monthCn + lunarDate.dayCn : lunarDate.dayCn}
				  </div>
					<div style="float:right">${date.getDate()}日</div>`
			};
		} else {
			return {
				html: `${date.getDate()}日`
			};
		}
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
