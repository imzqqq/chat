/**
 * Basic type for an attachment, usually on Statuses
 */
export type Attachment = {
    id: string;
    type: "unknown" | "image" | "gifv" | "audio" | "video";
    url: string;
    remote_url: string | null;
    preview_url: string;
    text_url: string | null;
    meta: any | null;
    description: string | null;
};
