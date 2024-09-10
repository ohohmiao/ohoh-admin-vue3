import http from "@/api";

export interface CaptchaImageResult {
	uuid: string;
	img: string;
}

export const captchaImageApi = () => {
	return http.get<CaptchaImageResult>("/captchaImage");
};
