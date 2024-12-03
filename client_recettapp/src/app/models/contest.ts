import { ContestCategory } from "./contest-category";

export interface Contest {
    id: number,
    title: string,
    max_participants: number,
    start_date?: Date,
    end_date?: Date,
    status: string,
    category?: ContestCategory
}
