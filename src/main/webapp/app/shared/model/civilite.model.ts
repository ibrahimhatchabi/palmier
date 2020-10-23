export interface ICivilite {
  id?: number;
  code?: number;
  libelle?: string;
}

export class Civilite implements ICivilite {
  constructor(public id?: number, public code?: number, public libelle?: string) {}
}
