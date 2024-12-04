import { ContestCategory } from "./contest-category";

export interface Contest {
    id?: number,
    title: string,
    max_participants: number,
    start_date?: string,
    end_date?: string,
    status: string,
    category?: ContestCategory
}
