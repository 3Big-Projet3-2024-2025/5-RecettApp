export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  date_registration: string;
  isBlocked: boolean;
  //registrations?: Entry[];
  //roles?: Role[];
}
