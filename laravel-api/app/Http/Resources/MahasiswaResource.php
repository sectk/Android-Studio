<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class MahasiswaResource extends JsonResource
{
    public $status;
    public $message;
    public $resource;

    public function __construct($status, $message, $resource)
    {
        parent::__construct($resource);
        $this->message = $message;
        $this->status = $status;
    }

    public function toArray($request)
    {
        return [

            'success' => $this->status,
            'message' => $this->message,
            'data' => $this->resource,


        ];
    }
}
