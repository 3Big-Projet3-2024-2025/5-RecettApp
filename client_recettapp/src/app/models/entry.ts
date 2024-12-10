import { User } from "./users"
import { Contest } from "./contest"

export interface Entry {
    id ?: number,
    users ?: User,
    contest ?: Contest,
    status ?: string
}