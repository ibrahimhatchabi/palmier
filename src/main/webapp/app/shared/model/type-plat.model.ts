export interface ITypePlat {
  id?: number;
  code?: number;
  libelle?: string;
}

export class TypePlat implements ITypePlat {
  constructor(public id?: number, public code?: number, public libelle?: string) {}
}
